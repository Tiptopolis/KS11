package com.uchump.prime.Metatron.Lib._HTTP._Spark.http.matching;

import java.util.List;

import com.uchump.prime.Metatron.Lib._HTTP._Spark.FilterImpl;
import com.uchump.prime.Metatron.Lib._HTTP._Spark.Request;
import com.uchump.prime.Metatron.Lib._HTTP._Spark.RequestResponseFactory;
import com.uchump.prime.Metatron.Lib._HTTP._Spark.route.HttpMethod;
import com.uchump.prime.Metatron.Lib._HTTP._Spark.routematch.RouteMatch;

/**
 * Executes the after filters matching an HTTP request.
 */
final class AfterFilters {

    static void execute(RouteContext context) throws Exception {

        Object content = context.body().get();

        List<RouteMatch> matchSet = context.routeMatcher().findMultiple(HttpMethod.after,
                                                                        context.uri(),
                                                                        context.acceptType());

        for (RouteMatch filterMatch : matchSet) {
            Object filterTarget = filterMatch.getTarget();

            if (filterTarget instanceof FilterImpl) {

                if (context.requestWrapper().getDelegate() == null) {
                    Request request = RequestResponseFactory.create(filterMatch, context.httpRequest());
                    context.requestWrapper().setDelegate(request);
                } else {
                    context.requestWrapper().changeMatch(filterMatch);
                }

                context.responseWrapper().setDelegate(context.response());

                FilterImpl filter = (FilterImpl) filterTarget;
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