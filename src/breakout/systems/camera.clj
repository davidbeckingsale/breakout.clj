(ns breakout.systems.camera
  (:use [breakout.lib.core :only (all-e)]))

(defn renderer [ents]
    (doseq [e ents]
      ((get-in e [:renderable :fn]) e)))
