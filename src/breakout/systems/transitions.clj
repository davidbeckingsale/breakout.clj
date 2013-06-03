(ns breakout.systems.transitions
  (:use [breakout.levels.one :as level]))

(defn transition [scurrent snew]
  (if (and (= scurrent :menu)
           (= snew :running))
    (level/level)))
