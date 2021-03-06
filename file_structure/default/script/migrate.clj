(ns migrate
  (:require [conjure.migration.runner :as runner]
            [conjure.server.server :as server]))

(defn 
#^{:doc "Prints out how to use the migration command."}
  print-usage []
  (println "Usage: ./run.sh script/migrate.clj [VERSION=<version number>]"))

(defn
#^{:doc "Gets the version number from the passed in version parameter. If the given version string is nil, then this method returns Integer.MAX_VALUE. If the version parameter is invalid, then this method prints an error and returns nil."}
  version-number [version-string]
  (if version-string
    (let [version-sequence (re-find #"^VERSION=([0-9]+)" version-string)]
      (if (and version-sequence (seq version-sequence))
        (. Integer parseInt (nth version-sequence 1))
        (do 
          (println "Invalid parameter:" version-string)
          nil)))
    (. Integer MAX_VALUE)))

(let [version (first *command-line-args*)]
  (do
    (server/init)
    (runner/update-to-version (version-number version))))