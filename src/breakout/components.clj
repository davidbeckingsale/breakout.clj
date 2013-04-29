(ns breakout.components
  (:use [breakout.lib.macros :only (component)]))

(def colours {:red [0.78 0.28 0.28]
              :orange [0.78 0.42 0.23]
              :brown [0.71 0.48 0.19]
              :yellow [0.64 0.64 0.16]
              :green [0.28 0.63 0.18]
              :blue [0.26 0.28 0.78]
              :grey [0.56 0.56 0.56]
              :black [0.0 0.0 0.0]
              :blue-green [0.26 0.62 0.51]})

(component position [x y]
           :x x
           :y y)

(component velocity [x y]
           :x x
           :y y)

(component destroyed? [b]
           :destroyed b)

(component renderable [f]
           :fn f)

(component colour [c]
           :rgb (c colours))

(component keyboard [])

(component paddle-actions []
           :move-left false
           :move-right false)

(component size [x y]
           :x x
           :y y)
