package com.uchump.prime.Metatron.Lib._HTTP._Spark.http.matching;

import com.uchump.prime.Metatron.Lib._HTTP._Spark.Request;
import com.uchump.prime.Metatron.Lib._HTTP._Spark.RequestResponseFactory;
import com.uchump.prime.Metatron.Lib._HTTP._Spark.RouteImpl;
import com.uchump.prime.Metatron.Lib._HTTP._Spark.route.HttpMethod;
import com.uchump.prime.Metatron.Lib._HTTP._Spark.routematch.RouteMatch;

/**
 * Created by Per Wendel on 2016-01-28.
 */
final class Routes {

    static void execute(RouteContext context) throws Exception {

        Object content = context.body().get();

        RouteMatch match = context.routeMatcher().find(context.httpMethod(), context.uri(), context.acceptType());

        Object target = null;
        if (match != null) {
            target = match.getTarget();
        } else if (context.httpMethod() == HttpMethod.head && context.body().notSet()) {
            // See if get is mapped to provide default head mapping
            content =
                    context.routeMatcher().find(HttpMethod.get, context.uri(), context.acceptType())
                            != null ? "" : null;
        }

        if (target != null) {
            Object result = null;

            if (target instanceof RouteImpl) {
                RouteImpl route = ((RouteImpl) target);

                if (context.requestWrapper().getDelegate() == null) {
                    Request request = RequestResponseFactory.create(match, context.httpRequest());
                    context.requestWrapper().setDelegate(request);
                } else {
                    context.requestWrapper().changeMatch(match);
                }

                context.responseWrapper().setDelegate(context.response());

                Object element = route.handle(context.requestWrapper(), context.responseWrapper());
                if (!context.responseWrapper().isRedirected()) {
                	result = route.render(element);
                }
            }

            if (result != null) {
                content = result;

                if (content instanceof String) {
                    String contentStr = (String) content;

                    if (!contentStr.equals("")) {
                        context.responseWrapper().body(contentStr);
                    }
                }
            }
        }

        context.body().set(content);
    }

}