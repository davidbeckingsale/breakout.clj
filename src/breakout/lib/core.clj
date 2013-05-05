(ns breakout.lib.core)

(def entities (atom {}))
(def id (atom 1))

(defn add [entity]
  (swap! entities assoc @id (merge {:id @id} entity))
  (- (swap! id inc) 1))

(defn get-e [id]
  (get @entities id))

(defn cset [entity ks v]
  (let [e (get @entities (get entity :id))]
  (swap! entities assoc (get e :id) (assoc-in e ks v))))

(defn all-e [component]
     (filter (fn [x] (contains? x component)) (vals @entities)))

(defn first-e [component]
  (first (filter (fn [x] (contains? x component)) (vals @entities)))) 

(defn del [entity]
  (let [id (:id entity)]
    (swap! entities dissoc id)))
