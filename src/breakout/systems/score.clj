(ns breakout.systems.score
  (:use [breakout.lib.core :only (del first-e)]
        [breakout.lib.macros :only (? !')]))

(defn update [ents]
  (doseq [e ents]
    (let [game (first-e :game)
          score (? game :game :score)]
      (if (? e :destroyed? :destroyed)
        (do (!' game (+ score 1) :game :score)
            (println (? game :game :score))
            (del e))))))
