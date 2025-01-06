package com.harrison;

import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;

public class Main {
	public static void main(String[] args) throws InterruptedException {
		// 定义一个发布者
		SubmissionPublisher<String> publisher = new SubmissionPublisher<>();

		// 定义一个订阅者
		Flow.Subscriber<String> subscriber = new Flow.Subscriber<>() {
			Flow.Subscription subscription;
			@Override
			public void onSubscribe(Flow.Subscription subscription) {
				System.out.println(Thread.currentThread() + "---onSubscribe");
				this.subscription = subscription;
				subscription.request(1);
			}

			@Override
			public void onNext(String item) {
				System.out.println(Thread.currentThread() + "---onNext: " + item);
				// 取消订阅
				// subscription.cancel();
				subscription.request(1);
			}

			@Override
			public void onError(Throwable throwable) {
				System.out.println(Thread.currentThread() + "---onError：" + throwable.getMessage());
			}

			@Override
			public void onComplete() {
				System.out.println(Thread.currentThread() + "---onComplete");
			}
		};

		// 发布者订阅一个订阅者
		publisher.subscribe(subscriber);

		// 发布数据
		for(int i = 0; i < 10; i++) {
			if(i >= 9) {
				// 模拟抛一个异常
				publisher.closeExceptionally(new RuntimeException("数字大于9，不想干了"));
			} else {
				publisher.submit("Hello World " + i);
			}
			Thread.sleep(1000);
		}

		publisher.close();


	}
}