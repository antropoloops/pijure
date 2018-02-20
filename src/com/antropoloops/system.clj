(ns com.antropoloops.system
  (:require [com.stuartsierra.component :as component]
            [com.antropoloops.overtone :as overtone]
            [com.antropoloops.osc :as osc]
            [com.antropoloops.o-osc :as o-osc]
            [com.antropoloops.keyboard :as keyboard]))

(defn example-system []
  (component/system-map
   :keyboard-listener (component/using
                       (keyboard/new*)
                       {:osc-server :osc-server})
   :osc-server (component/using
                (o-osc/new* osc/path osc/in-port osc/out-port)
                {:overtone :overtone})
   :overtone (overtone/new*)))


(comment
  (def s (component/start (example-system)))
  (component/stop s)
  (send-message  (:osc-server s) path [5 "mensaje"])
  (def flute (sample "./resources/sample.wav"))
  (flute)
  (c/kill 41)

  )
