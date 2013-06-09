(ns breakout.ui.lose
  (:use [breakout.lib.core :only (add first-e)]
        [breakout.components :only (text position renderable tag)]
        [breakout.renderers :only (render-text)]))

(defn menu []
  (add (merge (position 160 200)
              (text "game over!" 48)
              (tag :lose-menu)
              (renderable render-text)))
  (add (merge (position 35 300)
              (text "press <space> to try again..." 26)
              (tag :lose-menu)
              (renderable render-text)))
  (add (merge (position 280 400)
              (text (str "score: " (get-in (first-e :game) [:game :score])) 28)
              (tag :lose-menu)
              (renderable render-text))))
