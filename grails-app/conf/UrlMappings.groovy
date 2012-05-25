class UrlMappings {

	static mappings = {
		"/$controller/$action?/$id?"{
			constraints {
				// apply constraints here
			}
		}

		"/" (controller: "subscription", action: "about")
		"500"(view:'/error')
	}
}
