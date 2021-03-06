(ns breakout.levels.harry
  (:use [breakout.components]
        [breakout.lib.physics :as phys]
        [breakout.renderers]
        [breakout.lib.core :only (add)]))

(defn level []
  ; ball
  (add (merge (position 400 500)
              (size 10 10)
              (velocity -3 3)
              (colour :red)
              (phys/simulate {:mass 1})
              (renderable render-ball)))
  ; ; paddle
  (add (merge (position 350 550)
              (size 80 15)
              (renderable render-paddle)
              (velocity 0 0)
              (colour :red)
              (destroyed? false)
              (phys/simulate {:mass 0})
              (paddle-actions)
              (keyboard)))
  ; scenery
  (add (merge (position 0 50)
              (size 30 500)
              (colour :grey)
              (phys/simulate {:mass 0 :static true})
              (velocity 0 0)
              (renderable render-wall)))
  (add (merge (position 770 50)
              (size 30 500)
              (colour :grey)
              (phys/simulate {:mass 0 :static true})
              (velocity 0 0)
              (renderable render-wall)))
  (add (merge (position 30 50)
              (size 740 30)
              (colour :grey)
              (phys/simulate {:mass 0 :static true})
              (velocity 0 0)
              (renderable render-wall)))
   (add (merge (position 0 550)
               (size 30 15)
               (colour :blue-green)
               (phys/simulate {:mass 0 :static true})
               (velocity 0 0)
               (renderable render-wall)))
  (add (merge (position 770 550)
              (size 30 15)
              (colour :red)
              (phys/simulate {:mass 0 :static true})
              (velocity 0 0)
              (renderable render-wall)))
  ;top row of bricks
  (add (merge (position 30 150)
              (size 14 100)
              (renderable render-brick)
              (destroyed? false)
              (phys/simulate {:mass 0 :static true :fragile true})
              (velocity 0 0)
              (colour :red)))
  (add (merge (position 44 200)
              (size 75 14)
              (renderable render-brick)
              (destroyed? false)
              (phys/simulate {:mass 0 :static true :fragile true})
              (velocity 0 0)
              (colour :red)))
  (add (merge (position 119 150)
              (size 14 100)
              (renderable render-brick)
              (destroyed? false)
              (phys/simulate {:mass 0 :static true :fragile true})
              (velocity 0 0)
              (colour :red))))
