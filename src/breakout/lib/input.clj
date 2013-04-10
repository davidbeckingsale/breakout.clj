(ns breakout.lib.input
  (:import (org.lwjgl.input Keyboard)))

(defn update-input [state]
  (let [paddle (:paddle state) ]
    (cond (Keyboard/isKeyDown (Keyboard/KEY_LEFT))
          (assoc-in state [:paddle :velocity] [-4 0])
          (Keyboard/isKeyDown (Keyboard/KEY_RIGHT))
          (assoc-in state [:paddle :velocity] [4 0])
          (Keyboard/isKeyDown (Keyboard/KEY_ESCAPE))
          (assoc-in state [:running] false)
          true state))) 
