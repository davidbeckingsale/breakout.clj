(ns breakout.levels.test
  (:use [breakout.components]
        [breakout.renderers]
        [breakout.lib.core :only (add)]
        [breakout.lib.physics :as phys]))

(defn level []
  ; ball
  (add (merge (position 600 370)
              (size 80 80)
              (velocity -1 0)
              (colour :red)
              (phys/simulate {:mass 1})
              (renderable render-ball)))
  (add (merge (position 300 345)
              (size 80 80)
              (renderable render-brick)
              (destroyed? false)
              (phys/simulate {:mass 0 :fragile true})
              (velocity 0 0)
              (colour :red))))
