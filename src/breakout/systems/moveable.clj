(ns breakout.systems.moveable
  (:import (org.lwjgl.input Keyboard))
  (use [breakout.lib.core :only (!)]))

(def keymap 
  {:left Keyboard/KEY_LEFT
   :right Keyboard/KEY_RIGHT
   :escape Keyboard/KEY_ESCAPE})

(defn key? [code]
  (Keyboard/isKeyDown (get keymap code)))

(defn keyboard [ents]
  (doseq [e ents]
    (! e [:paddle-actions :move-left] false)
    (! e [:paddle-actions :move-right] false)
    (cond (key? :left)
          (! e [:paddle-actions :move-left] true)
          (key? :right)
          (! e [:paddle-actions :move-right] true))))

(defn move [ents]
  (doseq [e ents]
    (let [left (get-in e [:paddle-actions :move-left])
          right (get-in e [:paddle-actions :move-right])]
      (cond left
            (! e [:velocity :x] -4)
            right
            (! e [:velocity :x] 4)
            :else
            (! e [:velocity :x] 0)))))
