package com.uchump.prime.Metatron.Lib._HTTP._Spark;
/**
 * Model And View class is used to set the name of the view and the model object
 * to be rendered.
 *
 * @author alex
 */
public class ModelAndView {

    /**
     * Model object.
     */
    private Object model;
    /**
     * View name used to render output.
     */
    private String viewName;

    /**
     * Constructs an instance with the provided model and view name
     *
     * @param model    the model
     * @param viewName the view name
     */
    public ModelAndView(Object model, String viewName) {
        super();
        this.model = model;
        this.viewName = viewName;
    }

    /**
     * @return the model object
     */
    public Object getModel() {
        return model;
    }

    /**
     * @return the view name
     */
    public String getViewName() {
        return viewName;
    }

}