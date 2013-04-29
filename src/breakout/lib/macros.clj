(ns breakout.lib.macros)

(defmacro component [name params & r]
  `(defn ~name ~params
     (hash-map ~(keyword name) (hash-map ~@r))))
