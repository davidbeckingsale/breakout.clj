(ns breakout.ui.win
  (:use [breakout.lib.core :only (add first-e)]
        [breakout.components :only (text position renderable tag)]
        [breakout.renderers :only (render-text)]))

(defn menu []
  (add (merge (position 200 200)
              (text "you won!" 48)
              (tag :win-menu)
              (renderable render-text)))
  (add (merge (position 30 300)
              (text "press <space> to try again..." 26)
              (tag :win-menu)
              (renderable render-text)))
  (add (merge (position 280 400)
              (text (str "score: " (get-in (first-e :game) [:game :score])) 28)
              (tag :win-menu)
              (renderable render-text))))
