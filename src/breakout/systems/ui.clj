(ns breakout.systems.ui
  (:use [breakout.lib.macros :only (!)]
        [breakout.lib.input :only (key?)]))

(defn keyboard [ents]
  (doseq [e ents]
    (if (key? :escape)
      (! e :game {:state :exit}))))
