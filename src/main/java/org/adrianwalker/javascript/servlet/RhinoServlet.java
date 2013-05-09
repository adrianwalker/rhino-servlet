package org.adrianwalker.javascript.servlet;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public final class RhinoServlet extends HttpServlet {

  private static final String GET_METHOD = "GET";
  private static final String POST_METHOD = "POST";
  private static final String GET_FUNCTION = "doGet";
  private static final String POST_FUNCTION = "doPost";
  private ScriptEngine engine;

  @Override
  public void init() throws ServletException {
    super.init();

    ScriptEngineManager manager = new ScriptEngineManager();
    engine = manager.getEngineByName("JavaScript");
    engine.put("servlet", this);
  }

  @Override
  protected void doGet(
          final HttpServletRequest request,
          final HttpServletResponse reponse) throws ServletException, IOException {

    doProcess(request, reponse);
  }

  @Override
  protected void doPost(
          final HttpServletRequest request,
          final HttpServletResponse reponse) throws ServletException, IOException {

    doProcess(request, reponse);
  }

  private void doProcess(
          final HttpServletRequest request,
          final HttpServletResponse response) throws FileNotFoundException, ServletException {

    String path = getPath(request);

    try {
      engine.eval(new FileReader(path));
    } catch (ScriptException se) {
      throw new ServletException(se);
    }

    String function = getFunction(request);

    Invocable inv = (Invocable) engine;
    try {
      inv.invokeFunction(function, request, response);
    } catch (ScriptException | NoSuchMethodException ex) {
      throw new ServletException(ex);
    }
  }

  private String getPath(final HttpServletRequest request) {

    String servletPath = request.getServletPath();

    if (servletPath == null || servletPath.isEmpty()) {
      servletPath = request.getPathInfo();
    }

    String realPath = getServletContext().getRealPath(servletPath);

    return realPath;
  }

  private String getFunction(final HttpServletRequest request) throws ServletException {

    String method = request.getMethod();
    switch (method) {
      case GET_METHOD:
        return GET_FUNCTION;
      case POST_METHOD:
        return POST_FUNCTION;
      default:
        throw new ServletException(
                String.format("Unsupported method '%s'", method));
    }
  }

  public void load(String filename) throws ScriptException {

    String realPath = getServletContext().getRealPath(filename);

    try {
      engine.eval(new FileReader(realPath));
    } catch (FileNotFoundException e) {
      throw new RuntimeException("Error loading javascript file: " + filename, e);
    }
  }
}
