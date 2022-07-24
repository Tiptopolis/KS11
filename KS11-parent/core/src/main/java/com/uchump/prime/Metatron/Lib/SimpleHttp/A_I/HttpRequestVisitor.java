package com.uchump.prime.Metatron.Lib.SimpleHttp.A_I;

import com.uchump.prime.Metatron.Lib.SimpleHttp.FormUrlEncodedMessage;
import com.uchump.prime.Metatron.Lib.SimpleHttp.Multipart;
import com.uchump.prime.Metatron.Lib.SimpleHttp.UnencodedStringMessage;

public interface HttpRequestVisitor {

    void visit(HttpGet message);

    void visit(HttpDelete message);

    void visit(FormUrlEncodedMessage formUrlEncodedMessage);

    void visit(UnencodedStringMessage unencodedStringMessage);
    
    void visit(Multipart multipart);

}