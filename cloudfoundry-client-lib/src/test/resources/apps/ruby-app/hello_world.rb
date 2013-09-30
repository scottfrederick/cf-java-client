require 'sinatra/base'
require 'json'

class HelloWorld < Sinatra::Base
  get "/" do
    instance = JSON.parse(ENV["VCAP_APPLICATION"])["instance_index"]
    "Hello, World!\nFrom instance #{instance}"
  end
end
