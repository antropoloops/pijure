(ns com.antropoloops.logger
  "utilities for getting, setting, restoring logging level to ns"
  (:require [clojure.tools.logging :as logging]
            [clojure.string :as string]
            [clojure.test :as t])
  (:import org.slf4j.LoggerFactory
           ch.qos.logback.classic.Logger))
 
(defn parent-ns [ns*]
  {:pre [(some? ns*)]}
  (when-let [ns-seq (butlast (string/split  (name ns*) #"\."))]
    (string/join "." ns-seq)))

(defn get-level
  "example: (get-level 'foo-ns)
   recursively find parent level if there is not a level for current ns"
  ([] (get-level "root"))
  ([ns]
   (let [context (LoggerFactory/getILoggerFactory)
         ^Logger logger (.getLogger context (name ns))]
     (if-let [level (.getLevel logger)]
       (-> level str .toLowerCase keyword)
       (if-let [parent-ns (parent-ns ns)]
         (get-level parent-ns)
         (get-level))))))

(defn set-level!
  "example: (set-level! 'foo-ns :info)"
  ([level] (set-level! "root" level))
  ([ns level]
   (let [level (or level (get-level)) 
         context (LoggerFactory/getILoggerFactory)
         l (.getLogger context (name ns))
         level (.toUpperCase (name level))]
     (logging/debug "Setting log level" ns level)
     (.setLevel l (eval (read-string (format "ch.qos.logback.classic.Level/%s" level))))
     (-> level str .toLowerCase keyword))))
