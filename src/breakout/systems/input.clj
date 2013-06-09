(ns breakout.systems.input
  (:import (org.lwjgl.input Keyboard)))

(defn keyboard [ents]
  (while (Keyboard/next)
    (doseq [e ents]
      (let [func (get-in e [:keyboard :fn])]
        (func e (Keyboard/getEventKeyState) (Keyboard/getEventKey))))))
