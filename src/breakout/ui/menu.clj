(ns breakout.ui.menu
  (:use [breakout.lib.core :only (add)]
        [breakout.components :only (text position renderable)]))
        ;[breakout.renderers :only (render-text)]))

(defn menu []
  ; Title
  (add (merge (text "Breakout") (position 400 300))))
       ;(renderable render-text)))
