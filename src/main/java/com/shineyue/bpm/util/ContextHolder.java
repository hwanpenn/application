package com.shineyue.bpm.util;

import org.springframework.web.context.WebApplicationContext;

public class ContextHolder
{
  private static ContextHolder singleton;
  private WebApplicationContext wac;
  private String webRoot;

  public static ContextHolder instance()
  {
    if (singleton == null) {
      singleton = new ContextHolder();
    }
    return singleton;
  }

  public synchronized void setApplicationContext(WebApplicationContext wac) {
    this.wac = wac;
    if (wac != null) {
      this.webRoot = wac.getServletContext().getRealPath("");
      if ((this.webRoot != null) && (this.webRoot.indexOf("\\") != -1))
        this.webRoot = this.webRoot.replaceAll("\\\\", "/");
    }
  }

  public String getWebRoot()
  {
    return this.webRoot;
  }

  public String getRealPath(String relativePath) {
    if ((relativePath != null) && (relativePath.indexOf("\\") != -1)) {
      relativePath = relativePath.replaceAll("\\\\", "/");
    }
    if (relativePath.startsWith("/")) {
      return this.webRoot + relativePath;
    }

    return this.webRoot + "/" + relativePath;
  }

  public WebApplicationContext getApplicationContext()
  {
    return this.wac;
  }

}
