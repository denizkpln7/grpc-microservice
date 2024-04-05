package com.denizkpln.bookservice.service;

import com.denizkpln.bookservice.dto.LibraryDto;
import com.example.grpccommon.BookServiceGrpc;
import com.example.grpccommon.CustomError;
import com.example.grpccommon.GetData;
import com.example.grpccommon.GetLibrary;
import com.google.protobuf.Any;
import com.google.protobuf.Descriptors;
import com.google.protobuf.Empty;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.rpc.Code;
import com.google.rpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.protobuf.StatusProto;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;
import java.util.concurrent.TimeUnit;

@Service
public class BookGrpcService {

    @GrpcClient(value = "grpc-devproblems-service")
    private BookServiceGrpc.BookServiceBlockingStub bookServiceBlockingStub;

    @GrpcClient(value = "grpc-devproblems-service")
    private BookServiceGrpc.BookServiceStub bookServiceStub;


    public GetLibrary getLibrary(Long id) {
        GetData getData = GetData.newBuilder().setId(id).build();
        GetLibrary libraryData = bookServiceBlockingStub.getLibraryData(getData);
        return libraryData;
    }

    public GetLibrary getExceptionLibrary() throws InvalidProtocolBufferException {
        GetLibrary getLibrary = null;
        try {
            getLibrary = bookServiceBlockingStub.findByActiveLibrary(GetData.newBuilder().setId(2L).build());
        } catch (StatusRuntimeException s) {
            Status status = StatusProto.fromThrowable(s);
            String notFound = Code.forNumber(status.getCode()).toString();
            System.out.println(notFound);
            for (Any any : status.getDetailsList()) {
                if (any.is(CustomError.class)) {
                    CustomError unpack = any.unpack(CustomError.class);
                    System.out.println(unpack);
                }
            }
        }
        return getLibrary;
    }


    public List<Map<Descriptors.FieldDescriptor, Object>> getAllLibrary() throws InterruptedException {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        final List<Map<Descriptors.FieldDescriptor, Object>> response = new ArrayList<>();
        bookServiceStub.getAllLibrary(Empty.newBuilder().build(), new StreamObserver<GetLibrary>() {
            @Override
            public void onNext(GetLibrary library) {
                response.add(library.getAllFields());
            }

            @Override
            public void onError(Throwable throwable) {
                countDownLatch.countDown();
            }

            @Override
            public void onCompleted() {
                countDownLatch.countDown();
            }
        });
        boolean await = countDownLatch.await(1, TimeUnit.MINUTES);
        return await ? response : Collections.emptyList();
    }

    public Map<String, Map<Descriptors.FieldDescriptor, Object>> getExpensiveBook() throws InterruptedException {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        final Map<String, Map<Descriptors.FieldDescriptor, Object>> response = new HashMap<>();
        StreamObserver<GetLibrary> responseObserver = bookServiceStub.findActiveLibrary(new StreamObserver<GetLibrary>() {
            @Override
            public void onNext(GetLibrary book) {
                response.put("Get active", book.getAllFields());
            }

            @Override
            public void onError(Throwable throwable) {
                countDownLatch.countDown();
            }

            @Override
            public void onCompleted() {
                countDownLatch.countDown();
            }
        });


        getLibraries().forEach(responseObserver::onNext);
        responseObserver.onCompleted();
        boolean await = countDownLatch.await(1, TimeUnit.MINUTES);
        return await ? response : Collections.emptyMap();
    }

    public static List<GetLibrary> getLibraries() {
        return new ArrayList<GetLibrary>() {
            {
                add(GetLibrary.newBuilder().setName("deneme1").setId(1L).build());
                add(GetLibrary.newBuilder().setName("deneme2").setId(2L).build());
            }
        };
    }


}
