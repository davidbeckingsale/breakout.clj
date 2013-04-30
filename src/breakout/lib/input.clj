(ns breakout.lib.input
  (:import (org.lwjgl.input Keyboard)))

(def keymap 
  {:left Keyboard/KEY_LEFT
   :right Keyboard/KEY_RIGHT
   :escape Keyboard/KEY_ESCAPE})

(defn key? [code]
  (Keyboard/isKeyDown (get keymap code)))
