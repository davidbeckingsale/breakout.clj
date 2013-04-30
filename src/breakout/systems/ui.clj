(ns breakout.systems.ui
  (:use [breakout.lib.core :only (!)]
        [breakout.lib.input :only (key?)]))

(defn keyboard [ents]
  (doseq [e ents]
    (if (key? :escape)
      (! e [:state :state] :exit))))
