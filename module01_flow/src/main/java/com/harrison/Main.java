package com.harrison;

import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;

public class Main {
	/**
	 * 自定义处理器，既是发布者，又是订阅者
	 */
	static class MyProcessor extends SubmissionPublisher<String> implements Flow.Processor<String, String> {
		private Flow.Subscription subscription;
		@Override
		public void onSubscribe(Flow.Subscription subscription) {
			System.out.println(Thread.currentThread() + "---onSubscribe");
			subscription.request(1);
			// 绑定订阅关系
			this.subscription = subscription;
		}

		@Override
		public void onNext(String item) {
			// 前缀加一个哈哈
			String newItem = "哈哈" + item;
			System.out.println(Thread.currentThread() + "---onNext: " + newItem);
			submit(newItem);
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
	}
	public static void main(String[] args) throws InterruptedException {
		// 定义一个发布者
		SubmissionPublisher<String> publisher = new SubmissionPublisher<>();

		// 定义一个订阅者
		Flow.Subscriber<String> subscriber = new Flow.Subscriber<>() {
			Flow.Subscription subscription;
			@Override
			public void onSubscribe(Flow.Subscription subscription) {
				System.out.println(Thread.currentThread() + "---onSubscribe  ---processor");
				this.subscription = subscription;
				subscription.request(1);
			}

			@Override
			public void onNext(String item) {
				System.out.println(Thread.currentThread() + "---onNext: " + item + "  ---processor");
				// 取消订阅
				// subscription.cancel();
				subscription.request(1);
			}

			@Override
			public void onError(Throwable throwable) {
				System.out.println(Thread.currentThread() + "---onError：" + throwable.getMessage() + "  ---processor");
			}

			@Override
			public void onComplete() {
				System.out.println(Thread.currentThread() + "---onComplete  ---processor");
			}
		};

		MyProcessor myProcessor = new MyProcessor();
		// 发布者订阅一个订阅者
		publisher.subscribe(myProcessor);
		myProcessor.subscribe(subscriber);

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