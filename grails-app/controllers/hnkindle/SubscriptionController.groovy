package hnkindle

class SubscriptionController {

	def manage = {}

	def about = {}

	def feedback = {}

	def heuristics = {}

	def s_f = {
		render
	}

	def subscribe = {
		def email = params.email
		def a = Subscription.findAllByEmail(params.email, [max:1])
		if(a.size() > 0) {
			if(a[0].subscribed) {
				flash.message = "You are already subscribed!"	
			} else {
				Subscription.withTransaction {
					a[0].subscribed = true
					a[0].save(flush:true)
				}
				flash.message = "Your subscription is enabled again."
			}
		} else {
			Subscription.withTransaction {
				Subscription s = new Subscription()
				s.email = email
				s.subscribed = true
				s.save(flush:true)			
			}			
			flash.message = "You email ${email} has been added to the subscription list! Hope you enjoy the service!"
		}
		redirect(action:manage)
	}

	def unsubscribe = {
		def email = params.email
		def subs = Subscription.findAllByEmail(email)
		subs.each {s->
			Subscription.withTransaction {
				s.subscribed = false
				s.save(flush:true)
			}			
		}
		flash.message = "You email ${email} has been taken off the subscription list. Your feedback is most welcome"
		redirect(action:manage)
	}

	def deleteAll = {
		def subscriptions = Subscription.list()
		subscriptions.each {s ->
			Subscription.withTransaction {
				s.delete(flush:true)
			}
		}
		render "Done"
	}
}