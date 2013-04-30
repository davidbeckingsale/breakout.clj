(ns breakout.lib.physics
  (:require [clojure.math.numeric-tower :as math])
  (:use [breakout.lib.core :only (all-e !)]
        [breakout.lib.macros :only (component)]))

(component simulate [p]
           :properties p)

(defn aabb [e]
  (let [position (:position e)
        size (:size e)]
    {:min position 
     :max (merge-with + position size)})) 

(defn sat? [a b]
  (let [abox (aabb a)
        bbox (aabb b) 
        amin (:min abox)
        amax (:max abox)
        bmin (:min bbox)
        bmax (:max bbox)]
  (cond (< (:y amax) (:y bmin))
        false
        (> (:y amin) (:y bmax))
        false
        (> (:x amin) (:x bmax))
        false
        (< (:x amax) (:x bmin))
        false
        :else true)))

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
        amass (get-in a [:simulate :properties :mass])
        bmass (get-in b [:simulate :properties :mass])]
    (if-not (>= nvel 0)
      (let [j (* (- 2) nvel)
            j (if (and (= amass 0)
                       (= bmass 0))
                0
                (/ j (+ amass bmass)))
            impulse {:x (* (:x n) j) :y (* (:y n) j)}]
        (! a [:velocity] {:x (- (:x avel) (* amass (:x impulse))) :y (- (:y avel) (* amass (:y impulse)))})
        (if (get-in b [:simulate :properties :fragile] false)
          (! b [:destroyed? :destroyed] true))
        (if (and (get a :paddle-actions false)
                 (get-in b [:simulate :properties :static] false))
          (! a [:velocity]  {:x 0 :y 0}))))))

(defn sweep [ents]
  (doseq [a (filter (fn [x] (not (get-in x [:simulate :properties :static] false))) ents)]
    (doseq [b (filter (fn [x] (and (not= (get a :id) (get x :id))
                                  (not (get-in x [:destroyed? :destroyed] false)))) ents)]
      (if (sat? a b)
        (resolution a b)))))

(defn reposition [ents]
  (doseq [e ents]
    (let [pos (:position e)
          vel (:velocity e)
          npos (merge-with + pos vel)]
      (! e [:position] npos))))

(defn step []
  (sweep (all-e :simulate))
  (reposition (all-e :velocity)))
