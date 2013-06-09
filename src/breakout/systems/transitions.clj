(ns breakout.systems.transitions
  (:use [breakout.levels.one :as level]
        [breakout.lib.core :only (tagged-e del first-e add all-e)]
        [breakout.components :only (position renderable size text tag)]
        [breakout.renderers :only (render-score render-lives render-text)]
        [breakout.lib.macros :only (!')])
  (:require [breakout.ui.lose :as lose]
            [breakout.ui.win :as win]))

(defn transition [scurrent snew]
  (if (and (= scurrent :menu)
           (= snew :running))
    (do 
      (level/level)
      (add (merge (position 500 20)
                  (renderable render-lives)
                  (size 10 10)))
      (!' (first-e :game) render-score :renderable :fn)
      (doseq [e (tagged-e :main-menu)]
        (del e))))
  (if (and (= scurrent :running)
           (= snew :lose))
    (do 
      (doseq [e (all-e)]
        (if (contains? e :game)
          ; stop rendering score
          (del e :renderable)
          ; delete all level-specific entities
          (del e)))
      (lose/menu)
      (!' (first-e :game) 0 :game :score)))
  (if (and (= scurrent :running)
           (= snew :win))
    (do (doseq [e (all-e)]
        (if (contains? e :game)
          ; stop rendering score
          (del e :renderable)
          ; delete all level-specific entities
          (del e)))
      (win/menu)
      (!' (first-e :game) 0 :game :score)))
  (if (and (= scurrent :win)
           (= snew :running))
    (do (level/level)
      (add (merge (position 500 20)
                  (renderable render-lives)
                  (size 10 10)))
      (!' (first-e :game) 3 :game :lives)
      (!' (first-e :game) 0 :game :score)
      (!' (first-e :game) render-score :renderable :fn)
      (doseq [e (tagged-e :win-menu)]
        (del e))))
  (if (and (= scurrent :lose)
           (= snew :running))
    (do (level/level)
      (add (merge (position 500 20)
                  (renderable render-lives)
                  (size 10 10)))
      (!' (first-e :game) 3 :game :lives)
      (!' (first-e :game) 0 :game :score)
      (!' (first-e :game) render-score :renderable :fn)
      (doseq [e (tagged-e :lose-menu)]
        (del e))))
  (if (and (= scurrent :running)
           (= snew :paused))
    (add (merge (position 310 300)
                (text "paused" 32)
                (renderable render-text)
                (tag :pause-menu))))
  (if (and (= scurrent :paused)
           (= snew :running))
    (doseq [e (tagged-e :pause-menu)]
      (del e))))
