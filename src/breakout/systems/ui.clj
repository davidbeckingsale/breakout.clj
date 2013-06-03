(ns breakout.systems.ui
  (:use [breakout.lib.macros :only (! !')]
        [breakout.lib.input :only (key?)])
  (:require [breakout.systems.transitions :as trans]))

(defn keyboard [ents]
  (doseq [e ents]
    (if (key? :escape)
      (! e :game {:state :exit}))
    (if (and (= (get-in e [:game :state]) :menu)
             (key? :space))
      (do (!' e :running :game :state)
          (trans/transition :menu :running)))
    (if (and (= (get-in e [:game :state]) :running)
             (key? :space))
      (!' e :pause :game :state))
    (if (and (= (get-in e [:game :state]) :pause)
                (key? :space))
      (!' e :running :game :state))))
