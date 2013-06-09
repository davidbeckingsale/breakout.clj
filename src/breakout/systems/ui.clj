(ns breakout.systems.ui
  (:use [breakout.lib.macros :only (! !')]
        [breakout.lib.input :only (key?)])
  (:require [breakout.systems.transitions :as trans]))

(defn keyboard [e keyState keyValue]
  (if keyState
    (cond
      (= keyValue (key? :escape))
      (!' e :exit :game :state)
      (and (= (get-in e [:game :state]) :menu)
           (= keyValue (key? :space)))
      (do (!' e :running :game :state)
          (trans/transition :menu :running))
      (and (= (get-in e [:game :state]) :running)
           (= keyValue (key? :space)))
      (do (!' e :paused :game :state)
          (trans/transition :running :paused))
      (and (= (get-in e [:game :state]) :paused)
           (= keyValue (key? :space)))
      (do (!' e :running :game :state)
          (trans/transition :paused :running))
      (and (= (get-in e [:game :state]) :lose)
           (= keyValue (key? :space)))
      (do (!' e :running :game :state)
          (trans/transition :lose :running))
      (and (= (get-in e [:game :state]) :lose)
           (= keyValue (key? :space)))
      (do (!' e :running :game :state)
          (trans/transition :win :running))
      )))
