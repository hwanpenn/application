package com.shineyue.bpm.http;

import org.apache.http.conn.HttpClientConnectionManager;

public class ClientEvictExpiredConnections {

/*    public static void main(String[] args) throws Exception {

    	PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(200);
        cm.setDefaultMaxPerRoute(20);
        new IdleConnectionEvictor(cm).start();
    }*/

    public static class IdleConnectionEvictor extends Thread {

        private final HttpClientConnectionManager connMgr;

        private volatile boolean shutdown;

        public IdleConnectionEvictor(HttpClientConnectionManager connMgr) {
            this.connMgr = connMgr;
        }

        @Override
        public void run() {
            try {
                while (!shutdown) {
                    synchronized (this) {
                        wait(5000);
                        connMgr.closeExpiredConnections();
                    }
                }
            } catch (InterruptedException ex) {
            }
        }

        public void shutdown() {
            shutdown = true;
            synchronized (this) {
                notifyAll();
            }
        }
    }

}
