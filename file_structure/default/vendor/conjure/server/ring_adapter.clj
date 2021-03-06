(ns conjure.server.ring-adapter
  (:import [java.io File])
  (:require [conjure.server.server :as server]
            [ring.file :as ring-file]))

(defn
#^{:doc "The ring function which actually calls the conjure server and returns a properly formatted 
request map."}
  call-server [req]
  (let [response (server/process-request req)]
    (if (map? response)
      response
      {:status  200
       :headers {"Content-Type" "text/html"}
       :body    response})))

(defn
#^{:doc "A Ring adapter function for Conjure."}
  conjure [req]
  ((ring-file/wrap (new File "public") call-server) req))