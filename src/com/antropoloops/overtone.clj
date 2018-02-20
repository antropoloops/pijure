(ns com.antropoloops.overtone
  (:require [com.stuartsierra.component :as component]
            [overtone.core :as o]))

(defprotocol AudioEngine
  (play [this file]))

(defrecord OvertoneServer []
  AudioEngine
  (play [this file]
    (println "playing " file (:sample this))
    
    (if-not @(:atom this)
      (o/node-start ((o/sample "./resources/sample.wav")))
      (o/node-pause ((o/sample "./resources/sample.wav"))))
    
    
    (reset! (:atom this) (not @(:atom this))))

  component/Lifecycle
  (start [component]
    (println ";; starting overtone" (o/boot-internal-server) (o/server-status))
    (assoc component
           :atom (atom false)
           :sample ((o/sample "./resources/sample.wav"))))
  (stop [component]
    (println ";; Stopping overtone" (o/kill-server))
    component))



(defn new* []
  (OvertoneServer.))

(comment
  (def s (o/sample "./resources/sample.wav"))
  (def p (s))
  (o/node-pause p)
  (:id p) (into {} p)
  (o/stop-player (o-live/to-sc-id p))
                                        ;(o-live/ 40)
  (o/stop-all)
  )
