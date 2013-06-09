(ns breakout.ui.menu
  (:use [breakout.lib.core :only (add)]
        [breakout.components :only (text position renderable tag)]
        [breakout.renderers :only (render-text)]))

(defn menu []
  ; Title
  (add (merge (position 200 200)
              (text "breakout!" 48)
              (tag :main-menu)
              (renderable render-text)))
  (add (merge (position 80 300)
              (text "press <space> to play..." 28)
              (tag :main-menu)
              (renderable render-text))))
