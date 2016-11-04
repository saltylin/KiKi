#!/usr/bin/env bash
JAVA_HOME=${JAVA_HOME:-"$(dirname $(which java))/.."}
JAVA=${JAVA:-"${JAVA_HOME}/bin/java"}

export KIKI_HOME="$( cd "$( dirname "${BASH_SOURCE[0]}" )/.." && pwd )"

KIKI_JAR=${KIKI_HOME}/kiki-server/target/kiki-server-0.0.1-jar-with-dependencies.jar
KIKI_CONF_DIR=${KIKI_HOME}/conf

${JAVA} -Xmx4g -cp ${KIKI_JAR} \
    -Dkiki.conf.file=${KIKI_CONF_DIR}/kiki.properties \
    -Dlog4j.configuration=file:${KIKI_CONF_DIR}/log4j.properties \
    cn.edu.nju.pasalab.kiki.server.KikiServer
