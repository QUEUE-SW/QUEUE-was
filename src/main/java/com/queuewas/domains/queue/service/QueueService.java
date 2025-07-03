package com.queuewas.domains.queue.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.queuewas.domains.queue.QueueConsumer;
import com.queuewas.domains.queue.QueueProducer;
import com.queuewas.domains.queue.domain.QueueUser;
import com.queuewas.domains.queue.dto.request.QueueJoinReq;
import com.queuewas.domains.queue.dto.response.QueueJoinRes;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class QueueService {

	private final QueueProducer queueProducer;
	private final QueueConsumer queueConsumer;

	public QueueJoinRes join(QueueJoinReq queueJoinReq) {
		QueueUser user = queueProducer.submitUser(queueJoinReq.token());
		return QueueJoinRes.from(user);
	}
}
