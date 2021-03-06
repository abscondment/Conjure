(ns test.test.test-builder
  (:use clojure.contrib.test-is
        conjure.test.builder)
  (:require [conjure.test.util :as util]
            [conjure.util.file-utils :as file-utils]
            [test-helper :as test-helper]))

(deftest test-find-or-create-functional-test-directory
  (is (nil? (util/find-functional-test-directory)))
  (test-helper/test-directory (find-or-create-functional-test-directory) util/functional-dir-name)
  (test-helper/test-directory (find-or-create-functional-test-directory) util/functional-dir-name)
  (is (file-utils/delete-all-if-empty (util/find-functional-test-directory))))

(deftest test-find-or-create-unit-test-directory
  (is (nil? (util/find-unit-test-directory)))
  (test-helper/test-directory (find-or-create-unit-test-directory) util/unit-dir-name)
  (test-helper/test-directory (find-or-create-unit-test-directory) util/unit-dir-name)
  (is (file-utils/delete-all-if-empty (util/find-unit-test-directory))))

(deftest test-find-or-create-view-unit-test-directory
  (is (nil? (util/find-view-unit-test-directory)))
  (test-helper/test-directory (find-or-create-view-unit-test-directory) util/unit-view-dir-name)
  (test-helper/test-directory (find-or-create-view-unit-test-directory) util/unit-view-dir-name)
  (is (file-utils/delete-all-if-empty (util/find-view-unit-test-directory) (util/find-unit-test-directory))))

(deftest test-find-or-create-controller-view-unit-test-directory
  (let [controller "test"]
    (is (nil? (util/find-controller-view-unit-test-directory controller)))
    (test-helper/test-directory (find-or-create-controller-view-unit-test-directory controller) controller)
    (test-helper/test-directory (find-or-create-controller-view-unit-test-directory controller) controller)
    (is (file-utils/delete-all-if-empty (util/find-controller-view-unit-test-directory controller) (util/find-view-unit-test-directory) (util/find-unit-test-directory)))))

(deftest test-find-or-create-model-unit-test-directory
  (is (nil? (util/find-model-unit-test-directory)))
  (test-helper/test-directory (find-or-create-model-unit-test-directory) util/unit-model-dir-name)
  (test-helper/test-directory (find-or-create-model-unit-test-directory) util/unit-model-dir-name)
  (is (file-utils/delete-all-if-empty (util/find-model-unit-test-directory) (util/find-unit-test-directory))))

(deftest test-find-or-create-fixture-directory
  (is (nil? (util/find-fixture-directory)))
  (test-helper/test-directory (find-or-create-fixture-directory) util/fixture-dir-name)
  (test-helper/test-directory (find-or-create-fixture-directory) util/fixture-dir-name)
  (is (file-utils/delete-all-if-empty (util/find-fixture-directory))))

(defn test-functional-test [controller]
  (is (not (. (util/functional-test-file controller) exists)))
  (is (test-helper/test-file (create-functional-test controller) (util/functional-test-file-name controller)))
  (is (nil? (create-functional-test controller)))
  (is (file-utils/delete-all-if-empty (util/functional-test-file controller))))

(deftest test-create-functional-test
  (let [functional-test-directory (find-or-create-functional-test-directory)]
    (test-functional-test "test")
    (test-functional-test "foo-bar")
    (is (file-utils/delete-all-if-empty functional-test-directory))))

(defn test-unit-test [controller action]
  (is (not (. (util/view-unit-test-file controller action) exists)))
  (is (test-helper/test-file (create-view-unit-test controller action) (util/view-unit-test-file-name action)))
  (is (nil? (create-view-unit-test controller action)))
  (is (file-utils/delete-all-if-empty (util/view-unit-test-file controller action))))

(deftest test-create-view-unit-test
  (let [controller "test"
        controller-test-directory (find-or-create-controller-view-unit-test-directory controller)]
    (test-unit-test controller "show")
    (test-unit-test controller "foo-bar")
    (is (file-utils/delete-all-if-empty controller-test-directory (util/find-view-unit-test-directory) (util/find-unit-test-directory)))))

(defn test-model-test [model]
  (let [model-unit-test (util/model-unit-test-file model)]
    (is (not (. model-unit-test exists)))
    (is (test-helper/test-file (create-model-unit-test model) (util/model-unit-test-file-name model)))
    (is (nil? (create-model-unit-test model)))
    (is (. model-unit-test delete))))

(deftest test-create-model-unit-test
  (let [model-test-directory (find-or-create-model-unit-test-directory)]
    (test-model-test "test")
    (test-model-test "foo-bar")
    (is (file-utils/delete-all-if-empty model-test-directory))))

(defn test-fixture [model]
  (let [fixture-file (util/fixture-file model)]
    (is (not (. fixture-file exists)))
    (is (test-helper/test-file (create-fixture model) (util/fixture-file-name model)))
    (is (nil? (create-fixture model)))
    (is (. fixture-file delete))))

(deftest test-create-fixture
  (let [fixture-directory (find-or-create-fixture-directory)]
    (test-fixture "test")
    (test-fixture "foo-bar")
    (is (file-utils/delete-all-if-empty fixture-directory))))