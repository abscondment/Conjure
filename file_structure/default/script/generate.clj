(ns generate 
  (:require [generators.fixture-generator :as fixture-generator]
            [generators.migration-generator :as migration-generator]
            [generators.view-generator :as view-generator]
            [generators.controller-generator :as controller-generator]
            [generators.controller-test-generator :as controller-test-generator]
            [generators.model-generator :as model-generator]
            [generators.model-test-generator :as model-test-generator]
            [generators.view-test-generator :as view-test-generator]))

(defn print-usage []
  (println "Usage: ./run.sh script/generate.clj <generate type> <generate params>*"))

(defn print-unknown-command [command]
  (println (str "Unknown command: " command))
  (print-usage))

(defn generate [command params]
  (cond 
    (. command equals "migration")
      (migration-generator/generate-migration params)
      
    (. command equals "view")
      (view-generator/generate-view params)
      
    (. command equals "view-test")
      (view-test-generator/generate-view-test params)
      
    (. command equals "controller")
      (controller-generator/generate-controller params)
      
    (. command equals "controller-test")
      (controller-test-generator/generate-controller-test params)
      
    (. command equals "model")
      (model-generator/generate-model params)

    (. command equals "model-test")
      (model-test-generator/generate-model-test params)
      
    (. command equals "fixture")
      (fixture-generator/generate-fixture params)
      
    true ; Default condition.
      (print-unknown-command command)))

(let [generate-command (first *command-line-args*)
      generate-type-params (rest *command-line-args*)]
  (if generate-command
    (generate generate-command generate-type-params)
    (print-usage)))