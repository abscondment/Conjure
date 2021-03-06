(ns generators.view-generator
  (:import [java.io File])
  (:require [conjure.view.builder :as builder]
            [conjure.view.util :as util]
            [conjure.util.file-utils :as file-utils]
            [conjure.util.loading-utils :as loading-utils]
            [generators.view-test-generator :as view-test-generator]))

(defn
#^{:doc "Prints out how to use the generate view command."}
  view-usage []
  (println "You must supply a controller and action name (Like hello-world).")
  (println "Usage: ./run.sh script/generate.clj view <controller> <action>"))
  
(defn
#^{:doc "Returns view content which sets up the standard view namespace and defines a view. The given inner-content is 
added to the body of the view code."}
  generate-standard-content [view-namespace inner-content]
  (str "(ns " view-namespace "
  (:use conjure.view.base)
  (:require [clj-html.core :as html]))

(defview []
  (html/html 
    " inner-content "))"))
  
(defn
#^{:doc "Generates the view content and saves it into the given view file."}
  generate-file-content
    ([view-file controller] (generate-file-content view-file controller nil))
    ([view-file controller content]
      (let [view-namespace (util/view-namespace controller view-file)
            view-content (str (if content content 
(generate-standard-content view-namespace (str "[:p \"You can change this text in app/views/" (loading-utils/dashes-to-underscores controller) "/" (. view-file getName) "\"]"))))]
        (file-utils/write-file-content view-file view-content))))

(defn
#^{:doc "Creates the view file associated with the given controller and action."}
  generate-view-file
    ([controller action] (generate-view-file controller action nil))
    ([controller action content]
      (if (and controller action)
        (let [view-directory (util/find-views-directory)]
          (if view-directory
            (do 
              (let [controller-directory (builder/find-or-create-controller-directory view-directory controller)
                    view-file (builder/create-view-file controller-directory action)]
                (if view-file
                  (generate-file-content view-file controller content)))
              (view-test-generator/generate-unit-test controller action))
            (do
              (println "Could not find views directory.")
              (println view-directory))))
        (view-usage))))
        
(defn 
#^{:doc "Generates a migration file for the migration name given in params."}
  generate-view [params]
  (generate-view-file (first params) (second params)))