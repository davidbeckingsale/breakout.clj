(ns breakout.lib.physics
  (:require [clojure.math.numeric-tower :as math])
  (:use [breakout.lib.core :only (all-e)]
        [breakout.lib.macros :only (component ? !)]))

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
        apos {:x (+ (:x amin) (/ (? a :size :x) 2))
              :y (+ (:y amin) (/ (? a :size :y) 2))}
        bpos {:x (+ (:x bmin) (/ (? b :size :x) 2))
              :y (+ (:y bmin) (/ (? b :size :y) 2))}
        n {:x (- (:x bpos) (:x apos)) :y (- (:y bpos) (:y apos))}
        exa (/ (? a :size :x) 2)
        exb (/ (? b :size :x) 2)
        xoverlap (- (+ exa exb) (math/abs (:x n)))]
    (if (> xoverlap 0)
      (let [exa (/ (? a :size :y) 2)
            exb (/ (? b :size :y) 2)
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
        avel (:velocity a)
        bvel (:velocity b)
        vel {:x (- (:x bvel) (:x avel)) :y (- (:y bvel) (:y avel))}
        nvel (+ (* (:x n) (:x vel)) (* (:y n) (:y vel)))
        amass (? a :simulate :properties :mass)
        bmass (? b :simulate :properties :mass)]
    (if-not (>= nvel 0)
      (let [j (* (- 2) nvel)
            j (if (and (= amass 0)
                       (= bmass 0))
                0
                (/ j (+ amass bmass)))
            impulse {:x (* (:x n) j) :y (* (:y n) j)}]
        (! a :velocity {:x (- (:x avel) (* amass (:x impulse))) :y (- (:y avel) (* amass (:y impulse)))})
        (if (? b :simulate :properties :fragile)
          (! b :destroyed? {:destroyed true}))
        (if (and (? a :paddle-actions)
                 (? b :simulate :properties :static))
          (! a :velocity {:x 0 :y 0}))))))

(defn sweep [ents]
  (doseq [a (filter (fn [x] (not (? x :simulate :properties :static))) ents)]
    (doseq [b (filter (fn [x] (not= (:id a) (:id x))) ents)]
      (if (sat? a b)
        (resolution a b)))))

(defn reposition [ents]
  (doseq [e ents]
    (let [pos (:position e)
          vel (:velocity e)
          npos (merge-with + pos vel)]
      (! e :position npos))))

(defn step []
  (sweep (all-e :simulate))
  (reposition (all-e :velocity)))
