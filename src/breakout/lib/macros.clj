(ns breakout.lib.macros
  (:use [breakout.lib.core :only (cset first-e)]))

(defmacro component [name params & r]
  `(defn ~name ~params
     (hash-map ~(keyword name) (hash-map ~@r))))

(defmacro ? [entity & components] `(get-in ~entity [~@components] false))
(defmacro ! [entity k v] `(cset ~entity [~k] ~v))
(defmacro !' [entity v & ks] `(cset ~entity [~@ks] ~v))
