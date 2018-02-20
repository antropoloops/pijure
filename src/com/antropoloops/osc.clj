(ns com.antropoloops.osc
  (:require [com.stuartsierra.component :as component]
            [com.antropoloops.overtone :as overtone])
  
  (:import [com.illposed.osc OSCPortOut OSCPortIn OSCPort OSCMessage OSCListener]
           [java.net InetAddress]
           ))
;; https://github.com/hoijui/JavaOSC/blob/master/modules/core/src/main/java/com/illposed/osc/OSCPort.java
(defn close [what]
  (try
    (.close what)
    (catch Exception e (println :closing-exception e))
    ))

(defprotocol OSC
  (send-message [this path message]))

(defrecord OSCServer [path in-port out-port]
  component/Lifecycle
  (start [component]
    (println ";; starting osc server" (:overtone component))
    (let [receiver (try  (OSCPortIn. in-port)
                         (catch Exception e (println "problems starting receiver on port" in-port)))
          sender (try
                   (OSCPortOut. (InetAddress/getLocalHost) out-port)
                   (catch Exception e (println "problems starting sender on port" out-port)))
          res (assoc component
                     :receiver receiver
                     :sender sender)
          listener (reify OSCListener
                     (acceptMessage [this time message]
                       (let [args (into [] (.getArguments message))]
                         (println :listener-args!  args  (= 5 (first args)))
                         (when (= 5 (first args)) (overtone/play (:overtone this) (last args)))
                         (println (type  (first args)) args))))]
      (when (and receiver listener) 
        (.addListener receiver path listener)
        (.startListening receiver))
      res
      ))

  (stop [component]
    (println ";; Stopping osc server")
    (close (:sender component))
;;    (close (:receiver component))
    component)

  OSC
  (send-message [component path data]

    (.send (:sender component) (OSCMessage. path [5 "helliii"]))))

(def path "/sayhello")

(def out-port OSCPort/DEFAULT_SC_OSC_PORT)

(def in-port (OSCPort/defaultSCOSCPort))

(defn new* [path in-port out-port] (OSCServer. path in-port out-port))

(comment
  (def s (component/start (example-system)))
  (component/stop s)
  (send-message  (:osc-server s) path [5 "mensaje"])
  (def flute (sample "./resources/sample.wav"))
  (flute)
  (c/kill 41)

  )
