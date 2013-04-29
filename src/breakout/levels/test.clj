(ns breakout.levels.test
  (:use [breakout.components]
        [breakout.renderers]
        [breakout.lib.core :only (add)]))

(defn level []
  ; ball
  (add (merge (position 600 370)
              (size 80 80)
              (velocity -1 0)
              (colour :red)
              (body 1)
              (renderable render-ball)))
  ; ; paddle
  (add (merge (position 300 345)
              (size 80 80)
              (renderable render-paddle)
              (velocity 0 0)
              (colour :red)
              (body 0)
              (paddle-actions)
              (keyboard))))
