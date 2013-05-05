(ns breakout.systems.moveable 
  (use [breakout.lib.macros :only (! ?)]
       [breakout.lib.input :only (key?)]))

(defn keyboard [ents]
  (doseq [e ents]
    (! e :paddle-actions {:move-left false :move-right false})
    (cond (key? :left)
          (! e :paddle-actions {:move-left true :move-right false})
          (key? :right)
          (! e :paddle-actions {:move-left false :move-right true}))))

(defn move [ents]
  (doseq [e ents]
    (let [left (? e :paddle-actions :move-left)
          right (? e :paddle-actions :move-right)]
      (cond left
            (! e :velocity {:x -4 :y 0})
            right
            (! e :velocity {:x 4 :y 0})
            :else
            (! e :velocity {:x 0 :y 0})))))
