package com.uchump.prime.Metatron.Lib._HTTP._Spark.http.matching;

import java.util.List;

import com.uchump.prime.Metatron.Lib._HTTP._Spark.FilterImpl;
import com.uchump.prime.Metatron.Lib._HTTP._Spark.Request;
import com.uchump.prime.Metatron.Lib._HTTP._Spark.RequestResponseFactory;
import com.uchump.prime.Metatron.Lib._HTTP._Spark.route.HttpMethod;
import com.uchump.prime.Metatron.Lib._HTTP._Spark.routematch.RouteMatch;

/**
 * Executes the Before filters matching an HTTP request.
 */
final class BeforeFilters {

    static void execute(RouteContext context) throws Exception {
        Object content = context.body().get();

        List<RouteMatch> matchSet = context.routeMatcher().findMultiple(HttpMethod.before, context.uri(), context.acceptType());

        for (RouteMatch filterMatch : matchSet) {
            Object filterTarget = filterMatch.getTarget();

            if (filterTarget instanceof FilterImpl) {
                Request request = RequestResponseFactory.create(filterMatch, context.httpRequest());

                FilterImpl filter = (FilterImpl) filterTarget;

                context.requestWrapper().setDelegate(request);
                context.responseWrapper().setDelegate(context.response());

                filter.handle(context.requestWrapper(), context.responseWrapper());

                String bodyAfterFilter = context.response().body();

                if (bodyAfterFilter != null) {
                    content = bodyAfterFilter;
                }
            }
        }

        context.body().set(content);
    }

}