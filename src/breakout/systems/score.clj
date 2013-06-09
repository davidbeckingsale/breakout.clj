(ns breakout.systems.score
  (:use [breakout.lib.core :only (del first-e)]
        [breakout.lib.macros :only (? ! !')]
        [breakout.systems.transitions :as trans])
  (:require [breakout.world :as world]))

(defn update [ents]
  (doseq [e ents]
    (let [game (first-e :game)
          score (? game :game :score)]
      (if (? e :destroyed? :destroyed)
        (do (!' game (+ score 1) :game :score)
            (del e)))))
  (if (= (? (first-e :game) :game :score) 60)
    (do (!' (first-e :game) :win :game :state)
        (trans/transition :running :win))))

(defn lives [ents]
  (doseq [e ents]
    (let [game (first-e :game)
          lives (? game :game :lives)
          y (? e :position :y)]
      (if (> y (second world/screen-size))
        (do (!' game (- lives 1) :game :lives)
            (if (<= (- lives 1) 0)
              (do (!' game :lose :game :state)
                  (trans/transition :running :lose))
              (do (!' game :paused :game :state)
                  (!' e 400 :position :y)
                  (!' e 400 :position :x)
                  (! e :velocity {:x 3 :y -3}))))))))
