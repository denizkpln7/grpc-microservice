package com.denizkpln.libraryservice.service;

import com.denizkpln.libraryservice.exception.CustomErrorException;
import com.denizkpln.libraryservice.model.Library;
import com.example.grpccommon.BookServiceGrpc;
import com.example.grpccommon.CustomError;
import com.example.grpccommon.GetData;
import com.example.grpccommon.GetLibrary;
import com.google.protobuf.Empty;
import io.grpc.Metadata;
import io.grpc.Status;
import io.grpc.protobuf.ProtoUtils;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.List;


@GrpcService
@Slf4j
@RequiredArgsConstructor
public class LibraryGrpcService extends BookServiceGrpc.BookServiceImplBase {

    private final LibraryService libraryService;

    @Override
    public void getLibraryData(GetData request, StreamObserver<GetLibrary> responseObserver) {
        Library library=libraryService.findById(request.getId());
        GetLibrary getLibrary=GetLibrary.newBuilder()
                .setId(library.getId())
                .setName(library.getName())
                .build();
        responseObserver.onNext(getLibrary);
        responseObserver.onCompleted();
    }

    @Override
    public void getAllLibrary(Empty request, StreamObserver<GetLibrary> responseObserver) {
        List<Library> list=libraryService.findAll();
        for (Library library:list){
            GetLibrary getLibrary=GetLibrary.newBuilder().setName(library.getName()).setId(library.getId()).build();
            responseObserver.onNext(getLibrary);
        }
        responseObserver.onCompleted();
    }

    @Override
    public StreamObserver<GetLibrary> findActiveLibrary(StreamObserver<GetLibrary> responseObserver) {
        return new StreamObserver<GetLibrary>() {
            GetLibrary library=null;
            long priceStack=0;

            @Override
            public void onNext(GetLibrary getLibrary) {
                if (getLibrary.getId()>priceStack){
                    priceStack= getLibrary.getId();
                    library=getLibrary;
                }

            }

            @Override
            public void onError(Throwable throwable) {
                responseObserver.onError(throwable);
            }

            @Override
            public void onCompleted() {
                responseObserver.onNext(library);
                responseObserver.onCompleted();
            }
        };
    }


    @Override
    public void findByActiveLibrary(GetData request, StreamObserver<GetLibrary> responseObserver) {

        if (request.getId()==2){
            throw new CustomErrorException("not found library");
        }else {
            responseObserver.onNext(GetLibrary.newBuilder().setId(1L).setName("Deneme").build());
        }

        responseObserver.onCompleted();
    }
}
