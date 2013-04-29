(ns breakout.lib.physics
  (require [clojure.math.numeric-tower :as math])
  (use [breakout.lib.core :only (all-e !)]))

(defn sat? [[amin amax :as a] [bmin bmax :as b]]
  (cond (< (second amax) (second bmin))
        false
        (> (second amin) (second bmax))
        false
        (> (first amin) (first bmax))
        false
        (< (first amax) (first bmin))
        false
        :else true))

(defn normal [a b]
  (let [amin (:position a)
        bmin (:position b)
        apos {:x (+ (:x amin) (/ (get-in a [:size :x]) 2))
              :y (+ (:y amin) (/ (get-in a [:size :y]) 2))}
        bpos {:x (+ (:x bmin) (/ (get-in b [:size :x]) 2))
              :y (+ (:y bmin) (/ (get-in b [:size :y]) 2))}
        n {:x (- (:x bpos) (:x apos)) :y (- (:y bpos) (:y apos))}
        exa (/ (get-in a [:size :x]) 2)
        exb (/ (get-in b [:size :x]) 2)
        xoverlap (- (+ exa exb) (math/abs (:x n)))]
    (if (> xoverlap 0)
      (let [exa (/ (get-in a [:size :y]) 2)
            exb (/ (get-in b [:size :y]) 2)
            yoverlap (- (+ exa exb) (math/abs (:y n)))]
        (if (> yoverlap 0)
          (if (< xoverlap yoverlap)
            (if (< (:x n) 0)
              {:x -1 :y 0}
              {:x 1 :y 0})
            (if (< (:y n) 0)
              {:x 0 :y -1}
              {:x 0 :y 1}))
          {:x 0 :y 0}))
      {:x 0 :y 0})))

(defn resolution [a b]
  (let [n (normal a b)
        avel (get a :velocity)
        bvel (get b :velocity)
        vel {:x (- (:x bvel) (:x avel)) :y (- (:y bvel) (:y avel))}
        nvel (+ (* (:x n) (:x vel)) (* (:y n) (:y vel)))
        amass (get-in a [:body :mass])
        bmass (get-in b [:body :mass])]
    ; (println (str "Normal is: " n))
    (if-not (> nvel 0)
      (let [j (* (- 2) nvel)
            j (if (and (= amass 0)
                       (= bmass 0))
                0
                (/ j (+ amass bmass)))
            impulse {:x (* (:x n) j) :y (* (:y n) j)}]
        (! a [:velocity] {:x (- (:x avel) (* amass (:x impulse))) :y (- (:y avel) (* amass (:y impulse)))})
        (! b [:velocity] {:x (+ (:x bvel) (* bmass (:x impulse))) :y (+ (:y bvel) (* bmass (:y impulse)))}))))) 

(defn simulate [ents]
  (doseq [a ents]
    (let [[x y :as amin] [(get-in a [:position :x]) (get-in a [:position :y])]
          [x' y' :as amax] [(+ (get-in a [:size :x]) x)
                            (+ (get-in a [:size :y]) y)]]
      (doseq [b (filter (fn [x] (or (not= (get a :id) (get x :id)) 
                                    (not (get a :destroyed false)))) ents)]
        (let [[xb yb :as bmin] [(get-in b [:position :x]) (get-in b [:position :y])]
              [xb' yb' :as bmax] [(+ (get-in b [:size :x]) xb)
                                  (+ (get-in b [:size :y]) yb)]]
             (if (sat? [amin amax] [bmin bmax])
               (resolution a b)))))))

(defn reposition [ents]
  (doseq [e ents]
    (let [[x y] [(get-in e [:position :x]) (get-in e [:position :y])]
          [vx vy] [(get-in e [:velocity :x]) (get-in e [:velocity :y])]
          [x' y'] [(+ x vx) (+ y vy)]]
      (! e [:position :x] x')
      (! e [:position :y] y'))))

(defn step []
  (simulate (all-e :body))
  (reposition (all-e :velocity)))
