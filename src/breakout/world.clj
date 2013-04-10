(ns breakout.world)

(def world-size [8 5])
(def screen-size [800 600])

(defrecord World [bricks ball paddle running])
(defrecord Brick [position colour destroyed])
(defrecord Paddle [position velocity])
(defrecord Ball [position velocity])

(defn init-paddle []
  (->Paddle [350 580] [0 0]))

(defn init-ball []
  (->Ball [400 400] [-2 2]))

(def bricks
  [(->Brick nil [1.0 0.0 0.0] nil)    ; red
   (->Brick nil [0.0 1.0 0.0] nil)    ; green
   (->Brick nil [0.0 0.0 1.0] nil)    ; blue
   (->Brick nil [1.0 0.5 0.0] nil)    ; orange
   (->Brick nil [1.0 0.0 1.0] nil)    ; purple
   (->Brick nil [0.0 1.0 1.0] nil)])  ; violet

(defn random-bricks []
  (let [[cols rows] world-size
        [bx by] [100 60]]
    (for [row (range 0 rows)]
      (for [col (range 0 cols)]
        (let [x (* bx col)
              y (* by row)]
          (assoc (rand-nth bricks) :position [x y]))))))

(defn random-world []
  (->World (random-bricks) (init-ball) (init-paddle) true))
