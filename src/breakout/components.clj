(ns breakout.components
  (:use [breakout.lib.macros :only (component)])
  (:import (org.newdawn.slick.opengl Texture TextureLoader)
           (org.newdawn.slick.util ResourceLoader)))

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

(component game [state score lives]
           :state state
           :score score
           :lives lives)

(component velocity [x y]
           :x x
           :y y)

(component destroyed? [b]
           :destroyed b)

(component renderable [f]
           :fn f)

(component colour [c]
           :rgb (c colours))

(component keyboard [f]
           :fn f)

(component paddle-actions []
           :move-left false
           :move-right false)

(component size [x y]
           :x x
           :y y)

(component texture [uri]
           :texture (TextureLoader/getTexture 
                      "PNG" 
                      (ResourceLoader/getResourceAsStream uri)))

(component text [txt s]
           :string txt
           :size s)

(component tag [nm]
           :tag nm)
