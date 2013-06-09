(ns breakout.lib.input
  (:import (org.lwjgl.input Keyboard)))

(def keymap 
  {:left Keyboard/KEY_LEFT
   :right Keyboard/KEY_RIGHT
   :escape Keyboard/KEY_ESCAPE
   :space Keyboard/KEY_SPACE})

(defn key? [code]
  (get keymap code))
